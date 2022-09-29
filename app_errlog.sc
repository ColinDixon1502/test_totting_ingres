#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int app_errlog(
		char *app, 
		char *branch, 
		char *user, 
		char *proc, 
		char *message
	      )

{
    char    ebuff[500];
    int     i;
    
    /* Remove any newlines from message string */
    for (i=0; i < (int) strlen(message); i++)
	if (message[i]==13)
	    message[i]=32;
    
    /* Set up string to send to errorlog */        
    sprintf(ebuff,"%s %s (%s) : %s", branch, user, proc, message);
    
    /* Call errlog routine */
    return(errlog(ebuff,app,0));
}
