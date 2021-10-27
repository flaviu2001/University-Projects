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
    s db 1, 4, 2, 4, 8, 2, 1, 1
    lens equ $-s
    d times lens db 0
    lend db 0
    nr db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s
        mov edi, d
        mov ecx, lens
        here1:
            mov byte[nr], 0
            cmp esi, s
            jz here2
                mov eax, d
                push ecx
                mov ecx, [lend]
                here3:
                    inc eax
                    mov bl, [esi]
                    cmp bl, [eax-1]
                    jnz here4
                        inc byte[nr]
                    here4:
                    cmp bl, [eax-1]
                loopne here3
                pop ecx
            here2:
            lodsb
            cmp byte[nr], 0
            jnz here5
                stosb
                inc byte[lend]
            here5:
        loop here1
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
