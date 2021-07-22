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
    ; Acest program calculeaza (a*b+2)/(a+7-c)+d+x signed
    ; a,c-byte; b-word; d-doubleword; x-qword
    a db 2
    b dw 10
    c db 31
    d dd 16
    x dq 44

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al, [a]
        cbw
        imul word[b]
        add ax, 2
        adc dx, 0
        mov bl, [a]
        add bl, 7
        sub bl, [c]
        mov cx, ax
        mov al, bl
        cbw
        mov bx, ax
        mov ax, cx
        idiv bx
        push dx
        push ax
        pop eax
        add eax, [d]
        cdq
        add eax, [x]
        adc edx, [x+4]
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
