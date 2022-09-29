/*
** Inventory Management System CONTROL development using INGRES.
** ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
** MODULE NAME : asn_read_resend_invoice.sc 
**
** PURPOSE     : Extract binary large object from asn_invoice table.
**
** VERSION     : 1.0
**
** AUTHOR      : Bhavin Bhatt
**
** DATE        : 19/11/2009
**
** PARAMETERS  :   (i) Branch                            
**                (ii) Account No               
**               (iii) Pick Note                 
**                (iv) File Name                
--------------------------------------------------------------------------------
Ver | Date   | By   | Description
--------------------------------------------------------------------------------
1.0 |19/11/09| BB   | Original
--------------------------------------------------------------------------------
2.0 |27/01/10| ISS  | Allow process to run when called from abf routine
--------------------------------------------------------------------------------
*/

/*
** C include files
*/
#include "stdlib.h"
#include "string.h"
#include "fcntl.h"
#include "stdio.h"
#include "math.h"
#include "time.h"
#include "errno.h"
#include "sys/time.h"

/*
** ESQL include files
*/
EXEC SQL include SQLCA;

/*
** Procedures 
*/

int Get_Handler();

EXEC SQL BEGIN DECLARE SECTION;

  int              h_error;
  int              h_rows;
  char             h_err_text[500];

EXEC SQL END   DECLARE SECTION;

/*
** Variables  
*/

char asn_file_name[100];

int asn_read_resend_invoice (account_branch,account_no,picknote,asn_file_name1)

exec sql begin declare section;
  char *account_branch;
  char *account_no;
  long picknote;
  char *asn_file_name1;
exec sql end declare section;

{    
  sprintf(asn_file_name,"%s",asn_file_name1);

  EXEC SQL SELECT  picknote,invoice_binary
             INTO :picknote,datahandler(Get_Handler())
             FROM  asn_invoice
            WHERE  picknote = :picknote
              AND  account_branch = :account_branch
              AND  account_number = :account_no;

    EXEC SQL INQUIRE_SQL (:h_error = ERRORNO, :h_rows = ROWCOUNT, :h_err_text = ERRORTEXT);

    if (h_error != 0 ) 
    {
     printf ("Error in reading data %d %s\n", h_error, h_err_text);
     fflush (stdout);
     exit (1);
    }

    if (h_rows <= 0)
    {
     printf ("Error: Picknote not found in asn_invoice\n");
     fflush (stdout);
    }

  EXEC SQL COMMIT;

  return 0;
}

int Get_Handler()
{
  EXEC SQL BEGIN DECLARE SECTION;
       char seg_buf[1000];
       int seg_len;
       int data_end;
  EXEC SQL END DECLARE SECTION;

  int more_data;
  int write_cnt;
  int fd_hler = 0;

  data_end = 0;
  more_data = 1;

  if ((fd_hler = open(asn_file_name, O_CREAT+O_TRUNC+O_WRONLY, 0666)) == -1) 
  {
     printf ("failed opening Handler output file !!! error = %d \n",errno); 
     fflush (stdout);
     exit (1);
  }

  while (data_end == 0)
  {
    memset (&seg_buf, 0, 1000);

    EXEC SQL get data (:seg_buf = segment,
                       :seg_len = segmentlength,
                       :data_end = dataend)
                  with maxlength = 1000;

    EXEC SQL INQUIRE_SQL (:h_error = ERRORNO, :h_rows = ROWCOUNT, :h_err_text = ERRORTEXT);

    if (h_error != 0 ) 
    {
      printf ("Error in get data %d %s\n", h_error, h_err_text);
      fflush (stdout);
      return 1;
    }

    write_cnt = write (fd_hler, &seg_buf,seg_len);
    if (write_cnt <= 0) 
    {
        printf ("write count = %d \n",write_cnt); 
        fflush (stdout);
        if (write_cnt != 0)    /* error */
        {
         printf (" No data found in Handler input file !!! \n");
         exit (1);
	      }
    }
  }

  close (fd_hler);

  return 0;
}
