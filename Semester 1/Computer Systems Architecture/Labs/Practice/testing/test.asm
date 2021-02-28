bits 32

global start        

extern exit
import exit msvcrt.dll

extern printf
import printf msvcrt.dll

segment data use32 class=data

segment code use32 class=code
    start:
        
        push    dword 0
        call    [exit]
