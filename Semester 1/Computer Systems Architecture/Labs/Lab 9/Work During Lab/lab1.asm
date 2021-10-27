bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
extern fread
import fread msvcrt.dll
extern fopen
import fopen msvcrt.dll
extern fclose
import fclose msvcrt.dll
extern printf
import printf msvcrt.dll
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    n db "ana.txt", 0
    m db "r", 0
    d_f dd -1
    buff resb 100
    l equ $-buff
    cnt dd 0
    bytes dd 0
    format db "numarul de byti citit e %d",0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push m
        push n
        call [fopen]
        add esp, 4*2
        mov [d_f], eax
        cmp eax, 0
        je finish
            r:
                push dword[d_f]
                push l
                push 1
                push buff
                call [fread]
                add esp, 4*4
                add [bytes], eax
                cmp eax, l
            je r
        finish:
        push dword[bytes]
        push format
        call [printf]
        add esp, 4*2
        push dword[d_f]
        call [fclose]
        add esp, 4*1
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
