bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern fopen
import fopen msvcrt.dll
extern fprintf
import fprintf msvcrt.dll
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    f db "ana.txt", 0
    cuvant db "Ana %s are %d mere", 0
    m db "w", 0
    n db "Pop", 0
    d_f dd -1
    ; ...

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push m
        push f
        call [fopen]
        cmp eax, 0
        jz finish
            mov [d_f], eax
            add esp, 4*2
            push 3
            push n
            push cuvant
            push dword[d_f]
            call [fprintf]
            add esp, 4*4
        finish:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
