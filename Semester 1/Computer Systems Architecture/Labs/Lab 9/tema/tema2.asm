bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
extern scanf
extern fopen
extern fclose
extern strlen
extern fprintf
import scanf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import strlen msvcrt.dll
import fprintf msvcrt.dll
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    s times 20 resb 1
    format db "%s", 0
    format2 db "%s ", 0
    file_name db "fisier.txt", 0
    access_mode db "w", 0 
    file_descriptor dd -1
    ok db 0
    ; ...

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push dword access_mode     
        push dword file_name
        call [fopen]
        add esp, 4*2
        mov [file_descriptor], eax
        cmp eax, 0
        je finish
        back:
            push dword s
            push dword format
            call [scanf]
            add esp, 4*2
            mov al, [s]
            cmp al, '$'
            jz finish
            
            mov byte[ok], 0
            mov esi, s
            push s
            call [strlen]
            add esp, 4*1
            
            mov ecx, eax
            back2:
                lodsb
                cmp al, '0'
                jl here2
                cmp al, '9'
                jg here2
                mov byte[ok], 1
                here2:
            loop back2
            cmp byte[ok], 1
            jnz undeva
                push dword s
                push dword format2
                push dword [file_descriptor]
                call [fprintf]
                add esp, 4*2
            undeva:
        jmp back
        finish:
        push dword [file_descriptor]
        call [fclose]
        add esp, 4
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
