
#include <jni.h>
#include <string.h>
#ifdef _WIN32
#define PATH_SEPARATOR ';'
#else
#define PATH_SEPARATOR ':'
#endif

#define JVM_NOT_RUNNING -1
#define JAVA_ENV_ERROR  -2
#define CLASS_NOT_FOUND -3
#define METHOD_NOT_FOUND -4
#define JVM_CREATE_ERROR -5
#define HILIHASE_THREAD_ERROR -6
// #define JVM_NOT_RUNNING -1


JNIEnv *env = NULL;
JavaVM *jvm = NULL;
jclass cls = NULL;



jmethodID jvm_hilihase_step = NULL;
jmethodID jvm_echo = NULL;
jmethodID jvm_hilihase_register = NULL;
jmethodID jvm_hilihase_read = NULL;
jmethodID jvm_hilihase_drive = NULL;


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
    (*jvm)->DestroyJavaVM(jvm);
    jvm = NULL;
    return 0;
}

/**
 *  hilihase_init:
 *  initialise the Java framework
 *  argc must be 2
 *  argv is the path to the java class. (/home/ebenera/hilihase/jni)
 */
int  hilihase_init ( int argc, char* argv ){
    JavaVMOption options[1];
    JavaVMInitArgs vm_args;
    long status;

    char path[100];
    strcpy(path, "-Djava.class.path=");
    if(argc>1){
        strcat(path, argv);
    } else {
        strcat(path, ".");
    }
    options[0].optionString = path;
    // strcat(options[0].optionString, path); // "-Djava.class.path=.";
    memset(&vm_args, 0, sizeof(vm_args));
    vm_args.version = JNI_VERSION_1_2;
    vm_args.nOptions = 1;
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
        return HILIHASE_THREAD_ERROR;
    }
    status = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
    if (status == JNI_ERR) {
      hilihase_close();
      printf("Error: JNI_CreateJavaVM status:  %ld\n", status);
      return JVM_CREATE_ERROR;
    }
    printf("Info: Finding class... \n");
    cls = (*env)->FindClass(env, "hu/beton/hilihase/jfw/Sample2");
    if(0 == cls) {
        printf("Error: Sample2 class not found. (JVM options: %s)\n", path);
        return CLASS_NOT_FOUND;
    }
    printf("Info: Getting methods... \n");
    jvm_echo = (*env)->GetStaticMethodID(env, cls, "echo", "(I)I");
    if(0 == jvm_echo){
        printf("Error: echo method not found\n");
        return METHOD_NOT_FOUND;
    }
    jvm_hilihase_step = (*env)->GetStaticMethodID(env, cls, "hilihase_step", "(I)I");
    if(0 == jvm_hilihase_step){
        printf("Error: hilihase_step method not found\n");
        return METHOD_NOT_FOUND;
    }
    jvm_hilihase_read = (*env)->GetStaticMethodID(env, cls, "hilihase_read", "(IB)I");
    if(0 == jvm_hilihase_read){
        printf("Error: hilihase_read method not found\n");
        return METHOD_NOT_FOUND;
    }
    jvm_hilihase_drive = (*env)->GetStaticMethodID(env, cls, "hilihase_drive", "(I)B");
    if(0 == jvm_hilihase_drive){
        printf("Error: hilihase_drive method not found\n");
        return METHOD_NOT_FOUND;
    }
    jvm_hilihase_register = (*env)->GetStaticMethodID(env, cls, "hilihase_register", "(ILjava/lang/String;B)I");
    if(0 == jvm_hilihase_register){
        printf("Error: hilihase_register method not found\n");
        return METHOD_NOT_FOUND;
    }
    
   
    printf("Info: Initialization finished \n");
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
int  hilihase_register (int id, char* name, byte init_val){
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
byte  hilihase_drive (int id){
    int ret = check_callability(jvm_hilihase_drive);
    if ( ret<0 ){
        return (byte)ret;
    }
    return (byte)(*env)->CallStaticIntMethod(env, cls, jvm_hilihase_drive, id);
   
}


byte  hilihase_version (){
    return 0;
}

