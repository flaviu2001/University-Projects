bits 32

global start        

extern exit, fopen, fprintf, fclose, scanf
import exit msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import fclose msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data
    file_name db "practic.txt", 0
    access_mode db "w", 0
    fd resd 1
    format db "%d", 0
    format_w db "%d ", 0
    found_one db 0
    nothingfound db "There was no negative number with odd number of bits", 0
    x resd 1
    
segment code use32 class=code
    start:
        push access_mode
        push file_name
        call [fopen]
        add esp, 4*2
        mov [fd], eax
        cmp eax, 0
        je final
        
        back:
        push x
        push format
        call [scanf]
        add esp, 4*2
        cmp dword[x], 0
        je pre_final
        cmp dword[x], 0
        jnl not_neg
        
        mov eax, [x]
        mov ecx, 32
        mov edx, 0
        loop1:
        mov ebx, eax
        and ebx, 1
        add edx, ebx
        ror eax, 1
        loop loop1
        
        test edx, 1
        je not_neg
        
        push dword[x]
        push format_w
        push dword[fd]
        call [fprintf]
        add esp, 4*3
        mov byte[found_one], 1
        
        not_neg:
        jmp back
        
        pre_final:
        cmp byte[found_one], 1
        je nvm
        push nothingfound
        push dword[fd]
        call [fprintf]
        add esp, 4*2
        nvm:
        push dword[fd]
        call [fclose]
        add esp, 4*1
        final:
        push    dword 0
        call    [exit]
