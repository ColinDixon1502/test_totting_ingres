/*
**  File name : ana_validate.c
**
**  Author    : T. Farrell
**
**  Date      : 13/07/94
**
**
*******************************************************************************
**                                                                           **
**                  SALESACCOUNTS MAINTENANCE                                **
**                                                                           **
**    Routine to validate an ANA code check character                        **
**                                                                           **
**    Input parameter : 13 character ANA code                                **
**                                                                           **
**    Routine returns : 1   -   code is valid                                **
**                      0   -   code is not valid                            **
**                                                                           **
*******************************************************************************
*/
#include "stdio.h"
#include "stdlib.h"
ana_validate(ana_code) 
   char *ana_code;
{
   int check_sum = 0;
   int result = 0;
   int check_digit, ana_char, x;
    
   for(x=0; x<12; x++)
      {
	ana_char = (int)ana_code[x] - '0';
	 
	if ((x == 0) || (x == 3) || (x == 6) || (x == 9))
	   {
	     check_sum += ana_char;
	   }
	else if ((x == 1) || (x == 4) || (x == 7) || (x == 10))
	   {
	     check_sum = check_sum + (ana_char * 3);
	   }
	else
	   {     
	     check_sum = check_sum + (ana_char * 7);
	   }
	    
      }
	   
      check_digit = check_sum % 10;
       
      if (check_digit != 0)
	 check_digit = 10 - check_digit;
       
      if (check_digit == ((int)ana_code[12] - '0'))
	 result = 1;
	   
      return(result);    
    
} 
