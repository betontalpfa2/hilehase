/* MTI_DPI */

/*
 * Copyright 2002-2015 Mentor Graphics Corporation.
 *
 * Note:
 *   This file is automatically generated.
 *   Please do not edit this file - you will lose your edits.
 *
 * Settings when this file was generated:
 *   PLATFORM = 'linux'
 */
#ifndef INCLUDED_DPIHEADER
#define INCLUDED_DPIHEADER

#ifdef __cplusplus
#define DPI_LINK_DECL  extern "C" 
#else
#define DPI_LINK_DECL 
#endif

#include "svdpi.h"



DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_close();

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_echo1(
    int a);

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_echo2(
    int a);

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_init(
    int argc,
    const char* argv);

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_read(
    int id,
    char a,
    int sim_time);

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_register(
    int id,
    const char* name,
    char init_val);

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_start_tc(
    const char* str);

DPI_LINK_DECL DPI_DLLESPEC
int
hilihase_step(
    int curr_time);

DPI_LINK_DECL void
hilihase_drive2(
    int id,
    char data);

#endif 
