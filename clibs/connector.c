
#include <jni.h>
#include <string.h>
#ifdef _WIN32
#define PATH_SEPARATOR ';'
#else
#define PATH_SEPARATOR ':'
#endif

// #include "svdpi.h"
// extern void write(int, int);    // Imported from SystemVerilog
int slave_write(const int I1, const int I2)
{  
    return I1*2;
}


int java ( int argc, char *argv ) {
  JavaVMOption options[1];
  JNIEnv *env;
  JavaVM *jvm;
  JavaVMInitArgs vm_args;
  long status;
  jclass cls;
  jmethodID mid;
  jint square;
  jboolean not;

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
  status = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);

  if (status != JNI_ERR) {
    cls = (*env)->FindClass(env, "Sample2");
    if(cls !=0) {
        mid = (*env)->GetStaticMethodID(env, cls, "intMethod", "(I)I");
        if(mid !=0){
            square = (*env)->CallStaticIntMethod(env, cls, mid, 5);
            printf("Result of intMethod: %d\n", square);
        }
        mid = (*env)->GetStaticMethodID(env, cls, "booleanMethod", "(Z)Z");
        if(mid !=0){
            not = (*env)->CallStaticBooleanMethod(env, cls, mid, 1);
            printf("Result of booleanMethod: %d\n", not);
        }
    } else {
        printf("Error: Sample2 class not found\n");
    }

    (*jvm)->DestroyJavaVM(jvm);
    return 0;
  }
  printf("Error: JNI_CreateJavaVM status:  %d\n", status);
  return -1;
}