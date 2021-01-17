bits 32

global start
extern exit, digitsum, printf
import exit msvcrt.dll
import printf msvcrt.dll

;16, 255, 4, 254, 253, 18, 19, 18, 40, 49    -> 16 18 18
;17 18 254    -> 17 18
segment data use32 class=data
    sir dd 16, 255, 4, 254, 253, 18, 19, 18, 40, 49
    lensir equ ($-sir)/4   ;wtf
    newsir times 100 dd 0
    lennew dd 0
    format db "%d ", 0
    
segment code use32 class=code
    start:
        mov esi, sir
        mov ecx, lensir
        cld
        back:
            lodsd
            
            push ecx
            push esi
            
            push 10
            push eax
            call digitsum
            add esp, 4*2
            mov bl, al
            
            push ebx
            
            push 16
            sub esi, 4
            lodsd
            push eax
            call digitsum
            add esp, 4*2
            
            pop ebx
            mov bh, al
            
            pop esi
            pop ecx
            
            cmp bl, bh
            jna notcool
                mov edi, newsir
                times 4 add edi, [lennew] ;wtf
                sub esi, 4
                movsd
                inc dword[lennew]
            notcool:
        loop back
        
        cmp dword[lennew], 0
        je gata
        
        cmp dword[lennew], 1
        je aftersort
        mov dl, 1
        sort:
        cmp dl, 1
        jne aftersort
        mov dl, 0
        
        mov ecx, [lennew]
        dec ecx  ;wtf
        mov esi, newsir
        label1:
            mov eax, [esi]
            cmp eax, [esi+4]
            jna notgreater
            xchg eax, [esi+4]
            mov [esi], eax
            mov dl, 1
            notgreater:
            add esi, 4
        loop label1
        jmp sort
        aftersort:
        
        mov ecx, 3
        mov esi, newsir
        cmp dword[lennew], 3
        jnb nooo
        mov ecx, [lennew]
        nooo:
        
        
        back2:
        lodsd
        push ecx
        push eax
        push format
        call [printf]
        add esp, 4*2
        pop ecx
        loop back2
        
        gata:
        push    dword 0
        call    [exit]
