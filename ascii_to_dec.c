/*
 * =====================================================================================
 *
 *       Filename:  ascii_to_dec.c
 *
 *    Description:  Convert ASCII char to decimal equivalent for ABF
 *                  e.g. '0' -> 48, 'A' -> 65.
 *
 *        Version:  1.0
 *           Date:  21/07/2010
 *
 *         Author:  B. McMaster (BM)
 *        Company:  AAH Pharmaceuticals Ltd
 *
 * Change History:
 *        1.  21/07/2010  BM v1.0
 *          Created for Supply Chain Project WP4 R50.
 *
 * =====================================================================================
 */

#include <stdio.h>

/*  Prototype */
void ascii_to_dec (char *str, int *decimal);

void ascii_to_dec (char *str, int *decimal)
{
  *decimal = (int)str[0];
}

