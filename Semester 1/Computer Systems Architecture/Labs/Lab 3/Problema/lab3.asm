bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a dd 10
    b db 8
    c dw 4
    d db 1
    e dq 5
; our code starts here
segment code use32 class=code
    start:
        mov al, 10
        mov al, [b]
        mov ah, 0
        mov dx, 0
        div word[c]
        mov bx, ax
        mov al, 2
        mul byte[d]
        sub bx, ax
        mov ax, 0
        add bx, word[a]
        adc ax, word[a+2]
        mov dx, ax ;to look like with sign
        mov ax, bx
        push dx
        push ax
        pop eax
        mov edx, 0
        sub eax, dword[e]
        sbb edx, dword[e+4]
        ;with sign
        mov al, [b]
        cbw
        cwd
        idiv word[c]
        mov bx, ax
        mov al, 2
        imul byte[d]
        sub bx, ax
        mov ax, bx
        cwd
        add ax, word[a]
        adc dx, word[a+2]
        push dx
        push ax
        pop eax
        cdq
        sub eax, dword[e]
        sbb edx, dword[e+4]
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
