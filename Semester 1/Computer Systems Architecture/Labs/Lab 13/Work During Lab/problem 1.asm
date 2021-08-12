bits 32
global start        
extern exit
import exit msvcrt.dll
extern fopen
import fopen msvcrt.dll
extern fread
import fread msvcrt.dll
extern printf
import printf msvcrt.dll

segment data use32 class=data
    file_name db "file.txt", 0
    desc resd 1
    access_mode db "r", 0
    sir resb 1000
    q resb 1
    f times 257 dd 0
    isletter times 257 db 0
    format db "%c ", 0
    format2 db "%d", 10, 0
segment code use32 class=code
    start:
        mov ecx, 10
        mov eax, '0'
        lupa1:
        inc byte[isletter+eax]
        inc eax
        loop lupa1
        
        mov ecx, 26
        mov eax, 'a'
        lupa2:
        inc byte[isletter+eax]
        inc eax
        loop lupa2
        
        mov ecx, 26
        mov eax, 'A'
        lupa3:
        inc byte[isletter+eax]
        inc eax
        loop lupa3
        
        push access_mode
        push file_name
        call [fopen]
        add esp, 4*2
        
        cmp eax, 0
        je finish
            mov [desc], eax
            back:
            push dword[desc]
            push 1
            push 1
            push q
            call [fread]
            add esp, 4*4
            
            cmp eax, 0
            je done
                mov eax, 0
                mov al, [q]
                cmp byte[isletter+eax], 0
                jne after
                    mov bl, 4
                    mul bl
                    inc dword[f+eax]
                after:
                jmp back
            done:
            
        mov edx, 0
        mov eax, 0
        mov ecx, 256
        idk:
        cmp edx, [f+eax]
        jae notmax
            mov edx, [f+eax]
        notmax:
        add eax, 4
        loop idk
        
        cmp edx, 0
        je b4
        
        mov eax, 0
        mov ecx, 256
        idk2:
        cmp edx, [f+eax]
        jne noteq
            pusha
            mov ebx, 4
            div ebx
            push eax
            push format
            call [printf]
            add esp, 4*2
            popa
        noteq:
        add eax, 4
        loop idk2
        
        b4:
        push edx
        push format2
        call [printf]
        add esp, 4*2
        finish:
        push    dword 0
        call    [exit]
