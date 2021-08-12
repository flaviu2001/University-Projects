bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a db 10110011b
    b db 01110001b
    c db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al, [a]
        and al, 11100000b
        shr al, 5
        mov [c], al
        and byte[c], 11000111b
        and byte[c], 10111111b
        or byte[c], 01000000b
        and byte[c], 01111111b
        mov al, [b]
        not al
        and al, 00001000b
        shl al, 4
        or byte[c], al
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
