bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
extern prime
extern scanf
import scanf msvcrt.dll
extern printf
import printf msvcrt.dll
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    nr resd 1
    format db "%d", 0
    format2 db "%d ", 0
    nr2 dd 0
    v resd 100
    ; ...

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push nr
        push format
        call [scanf]
        add esp, 4*2
        
        mov ecx, [nr]
        jecxz finish
        back:
            push ecx
            push nr
            push format
            call [scanf]
            add esp, 4*2
            push dword[nr]
            call prime
            cmp eax, 1
            jne after
            mov edx, [nr2]
            mov ebx, [nr]
            mov [v+edx*4], ebx
            inc dword[nr2]
            after:
            pop ecx
        loop back
        mov eax, 0
        mov ecx, [nr2]
        jecxz finish
        back2:
        push ecx
        push eax
        push dword[v+eax]
        push format2
        call [printf]
        add esp, 4*2
        pop eax
        pop ecx
        add eax, 4
        loop back2
        finish:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
