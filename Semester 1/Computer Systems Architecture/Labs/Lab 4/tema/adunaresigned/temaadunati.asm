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
    ; Acest program calculeaza (a+b+d)-(a-c+d)+(b-c) signed
    a db 4
    b dw 10
    c dd 100
    d dq 102

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al, [a]
        cbw
        add ax, [b]
        cwde
        cdq
        add eax, [d]
        adc edx, [d+4]
        mov ebx, eax
        mov ecx, edx
        mov al, [a]
        cbw
        cwde
        sub eax, [c]
        cdq
        add eax, [d]
        adc edx, [d+4]
        sub ebx, eax
        sbb ecx, edx
        mov ax, [b]
        cwde
        sub eax, [c]
        add ebx, eax
        adc ecx, 0
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
