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
    ; Acest program calculeaza (a*b+2)/(a+7-c)+d+x unsigned
    a db 10
    b dw 3
    c db 1
    d dd 1
    x dq 99

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al, [a]
        mov ah, 0
        mul word[b]
        add ax, 2
        adc dx, 0
        mov bl, [a]
        add bl, 7
        sub bl, [c]
        mov cx, ax
        mov al, bl
        mov ah, 0
        mov bx, ax
        mov ax, cx
        div bx
        push dx
        push ax
        pop eax
        add eax, [d]
        mov edx, 0
        add eax, [x]
        adc edx, [x+4]
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
