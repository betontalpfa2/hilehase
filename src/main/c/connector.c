
#include <jni.h>
#include <string.h>
#include "hu_beton_hilihase_jfw_Sample2.h"
#ifdef _WIN32
#define PATH_SEPARATOR ';'
#else
#define PATH_SEPARATOR ':'
#endif
#include "svdpi.h"
#include "dpiheader.h"
#include "connector.h"
// #define JVM_NOT_RUNNING -1


JNIEnv *env = NULL;
JavaVM *jvm = NULL;
jclass cls = NULL;
int error_code = 0;


static svScope  calling_scope;
jmethodID jvm_hilihase_step = NULL;
jmethodID jvm_echo = NULL;
jmethodID jvm_hilihase_register = NULL;
jmethodID jvm_hilihase_read = NULL;
jmethodID jvm_hilihase_drive = NULL;
jmethodID jvm_hilihase_init = NULL;
jmethodID jvm_hilihase_start_tc = NULL;
jmethodID jvm_hilihase_close = NULL;


#define nOOptions 2


extern void hilihase_drive2(int id, char data);    // Imported from SystemVerilog


JNIEXPORT jint JNICALL Java_hu_beton_hilihase_jfw_Sample2_hilihase_1drve
  (JNIEnv * env, jobject obj, jint id, jbyte val){
    printf("@@@@@@@@@@@@@@@HERE I AM!!!!!!@@@@@@@@@@@@@");
    svSetScope(calling_scope);
    hilihase_drive2(id, val);
    return 0;
}

/**
 * TypeSign -- Java Type --	Native Type --	Description
 *     Z         boolean 	 jboolean 	     unsigned 8 bits
 *     B         byte 	     jbyte 	         signed 8 bits
 *     C         char 	     jchar 	         unsigned 16 bits
 *     S         short 	     jshort 	     signed 16 bits
 *     I         int 	     jint 	         signed 32 bits
 *     J         long 	     jlong 	         signed 64 bits
 *     F         float 	     jfloat 	     32 bits
 *     D         double      jdouble 	     64 bits
 *               void 	     void 	         not applicable
 *
 *     L    fully-qualified-class ; 	fully-qualified-class
 *     [    type 	type[]
 *  ( arg-types ) ret-type 	method type 
 */
// jmethodID mid;

typedef char byte;


int check_callability(jmethodID mid){
    if(0 == jvm){
        printf("ERROR: Java virtual machine is not running.\n");
        return JVM_NOT_RUNNING;
    }
    if(0 == env){
        printf("ERROR: Java env is not initialized.\n");
        return JAVA_ENV_ERROR;
    }
    if(0 == mid){
        printf("ERROR: Method is not initialized or not found.\n");
        return CLASS_NOT_FOUND;
    }
    return 0;
}

/**
 * hilihase_close
 * Closes the Java framework at the end of the simulation.
 */
int  hilihase_close ( ){
    if(jvm){
        printf("Destroying BASE from C...");
        fflush(stdout);
        error_code =  (*env)->CallStaticIntMethod(env, cls, jvm_hilihase_close, 0);    
        printf("Destroying JAVA...");
        fflush(stdout);
        (*jvm)->DestroyJavaVM(jvm);
        printf("    [  OK  ]\n");
        fflush(stdout);
    }
    jvm = NULL;
    return 0;
}

int __function_getter__(jmethodID* mid, const char* name, const char* decoration){
    *mid = (*env)->GetStaticMethodID(env, cls, name, decoration);
    if(0 == *mid){
        printf("Error: %s method not found\n", name);
        error_code = METHOD_NOT_FOUND;
        return error_code;
    }
    return 0;
}

/**
 *  hilihase_init:
 *  initialise the Java framework
 *  argc must be 2
 *  argv is the path to the java class. (/home/ebenera/hilihase/jni)
 */
int  hilihase_init ( int argc, const char* argv ){
    JavaVMOption options[nOOptions];
    JavaVMInitArgs vm_args;
    long status;

    calling_scope = svGetScope();
    char path[100];
    strcpy(path, "-Djava.class.path=");
    if(argc>1){
        strcat(path, argv);
    } else {
        strcat(path, ".");
    }
    
    options[0].optionString = "-Djava.library.path=/home/ebenera/hilihase/target/nar/jfw-1.0-SNAPSHOT-amd64-Linux-gpp-shared/lib/amd64-Linux-gpp/shared";
    options[1].optionString = path;
    // strcat(options[0].optionString, path); // "-Djava.class.path=.";
    memset(&vm_args, 0, sizeof(vm_args));
    vm_args.version = JNI_VERSION_1_2;
    vm_args.nOptions = nOOptions;
    vm_args.options = options;
    
    printf("Info: Creating JVM... (JVM options: %s)\n", path);
    
    /**
     *  This is a shared library.
     *  Current implemetation does not supports multi-user/multi-simulation.
     *  Following line is not thread-safe, but it is enough during the developing.
     *  Proposal is to start JVM for each simulator, whose identify themselfes
     *  with an id. 
     */
    if(NULL != jvm){
        printf("ERROR: JVM is already running. Do not call hilihase_init twice, but maybe another user uses HILIHASE, which is not supported.");
        fflush(stdout);
        return HILIHASE_THREAD_ERROR;
    }
    status = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
    if (status == JNI_ERR) {
      hilihase_close();
      printf("Error: JNI_CreateJavaVM status:  %ld\n", status);
        fflush(stdout);
      return JVM_CREATE_ERROR;
    }
    printf("Info: Finding class... \n");
    cls = (*env)->FindClass(env, "hu/beton/hilihase/jfw/Sample2");
    if(0 == cls) {
        printf("Error: Sample2 class not found. (JVM options: %s)\n", path);
        fflush(stdout);
        return CLASS_NOT_FOUND;
    }
    printf("Info: Getting methods... ");
    __function_getter__(&jvm_echo, "echo", "(I)I");
    __function_getter__(&jvm_hilihase_step, "hilihase_step", "(I)I");
    __function_getter__(&jvm_hilihase_read, "hilihase_read", "(IB)I");
    __function_getter__(&jvm_hilihase_register, "hilihase_register", "(ILjava/lang/String;B)I");
    __function_getter__(&jvm_hilihase_init, "hilihase_init", "(I)I");
    __function_getter__(&jvm_hilihase_start_tc, "hilihase_start_tc", "(Ljava/lang/String;)I");
    __function_getter__(&jvm_hilihase_close, "hilihase_close", "(I)I");
    
    if(error_code<0){
        printf("ERROR during getting methods!!! \n");
        fflush(stdout);
        return error_code;
    }
    printf("  [  OK  ]\n");
        
    printf("Info: Initialize Java framework (aka. Base)... \n");
    fflush(stdout);
    error_code =  (*env)->CallStaticIntMethod(env, cls, jvm_hilihase_init, 0);
   
    printf("Info: Initialization finished \n");
    fflush(stdout);
    return 0;
}

/**
 *  hilihase_step:
 *  At the beginnig of each time-slot simulator must call this function
 *  to inform the framework about the step.
 *  The return value can be:
 *      0: Nothing -- Go on
 *      1: Exit the simulator
 *      Negative: error
 */
int  hilihase_step ( int curr_time ){
    int ret = check_callability(jvm_hilihase_step);
    if ( ret<0 ){
        return ret;
    }
    return  (*env)->CallStaticIntMethod(env, cls, jvm_hilihase_step, curr_time);
    
    // printf("Error: hilihase_step method not found\n");
    // return -1;
}


/**
 * hilihase_echo1:
 *    Just for test. It does not uses Java. It just test the DPI aka.
 * the connection between the systemverilog simulator and the C native funtions.
 * It just exhoes the given parameter.
 */
int  hilihase_echo1 ( int a){
    return a;
}

/**
 * hilihase_echo2:
 *  Same as hilihase_echo1, just it uses and tests Java framework too.
 *    (You must initialise the Framework before calling this.)
 */
int  hilihase_echo2 ( int a){
    int ret = check_callability(jvm_echo);
    if ( ret<0 ){
        return ret;
    }
    return  (*env)->CallStaticIntMethod(env, cls, jvm_echo, a);
}

/**
 *   You must register all signals at the beginnig of the simulation with they init value.
 */
int  hilihase_register (int id, const char* name, byte init_val){
    int ret = check_callability(jvm_hilihase_register);
    if ( ret<0 ){
        return ret;
    }
    // char *buf = (char*)malloc(10);
    // strcpy(buf, "123456789"); // with the null terminator the string adds up to 10 bytes
    jstring jstrBuf = (*env)->NewStringUTF(env, name);
    // jstring NewStringUTF(JNIEnv *env, const char *name);
    return  (*env)->CallStaticIntMethod(env, cls, jvm_hilihase_register, id, jstrBuf, init_val);
    
}

/**
 * hilihase_read: Simulator must call this funtion on each signal each change 
 *   to inform the JAva framework about the state of signals
 *   Use registered id to identify the signals.
 */
int  hilihase_read (int id, byte a){
    int ret = check_callability(jvm_hilihase_read);
    if ( ret<0 ){
        return ret;
    }
    return  (*env)->CallStaticIntMethod(env, cls, jvm_hilihase_read, id, a);
    
}

/**
 *   hilihase_drive: 
 * This queries the signal with the gives signal if it changed in the current timeslot.
 */
// void  hilihase_drive (int id, char* val){
    // int ret = check_callability(jvm_hilihase_drive);
    // if ( ret<0 ){
        // return (byte)ret;
    // }
    // return (byte)(*env)->CallStaticIntMethod(env, cls, jvm_hilihase_drive, id);
   
// }


// extern void hilihase_drive2(int id, char data);    // Imported from SystemVerilog


// JNIEXPORT jint JNICALL Java_hu_beton_hilihase_jfw_Sample2_hilihase_1drve
  // (JNIEnv * env, jobject obj, jint id, jbyte val){
    // printf("@@@@@@@@@@@@@@@HERE I AM!!!!!!@@@@@@@@@@@@@");
    // hilihase_drive2(id, val);
    // return 0;
// }


// void hilihase_drive3(int id, char data){
    // hilihase_drive2(id, data);
// }


int hilihase_start_tc(const char*  tcName){
    
    int ret = check_callability(jvm_hilihase_start_tc);
    if ( ret<0 ){
        return ret;
    }
    return  (*env)->CallStaticIntMethod(env, cls, jvm_hilihase_start_tc, tcName);
    
}


byte  hilihase_version (){
    return 0;
}

