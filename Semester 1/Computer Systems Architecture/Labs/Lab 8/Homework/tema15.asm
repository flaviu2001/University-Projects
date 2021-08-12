bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    s dd 12345607h, 1A2B3C15h
    len equ ($-s)/4
    d times len*4 db 0
    ; ...

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ecx, len
        mov esi, s
        mov edi, d
        loop1:
           lodsb
           stosb
           lodsb
           stosb
           lodsb
           stosb
           lodsb
           stosb
        loop loop1
        mov edx, 1
        back:
        cmp edx, 1
        jnz finish
        mov edx, 0
        mov esi, d
        mov ecx, len*4-1
        loop2:
            mov al, [esi]
            mov bl, [esi+1] ;al, bl
            cmp al, bl
            ja here2
               mov edx, 1
               mov [esi+1], al
               mov [esi], bl
            here2:
            inc esi
        loop loop2
        jmp back
        finish:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
