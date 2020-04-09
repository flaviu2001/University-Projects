bits 32

global fun

segment code use32 class=code
    fun: ;void fun(int x, int *bytes, int *rank, int *len2, int *sum);
        push ebp
        mov ebp, esp
        ;[ebp+8] - x
        ;[ebp+12] - bytes
        ;[ebp+16] - rank
        ;[ebp+20] - len2
        ;[ebp+24] - sum
        
        mov eax, [ebp+8]
        mov ebx, 0
        mov bl, al ;max
        mov ecx, 3
        mov edx, 0
        mov dl, 4 ;rank
        mov dh, 4
        _back:
            ror eax, 8
            dec dh
            cmp al, bl
            jna _after
            mov bl, al
            mov dl, dh
            _after:
        loop _back
        
        mov eax, [ebp+24]
        add [eax], ebx
        
        mov edi, [ebp+12]
        mov eax, [ebp+20]
        add edi, [eax]
        mov al, bl
        stosb
        
        mov edi, [ebp+16]
        mov eax, [ebp+20]
        add edi, [eax]
        mov al, dl
        add al, '0'
        stosb
        
        mov eax, [ebp+20]
        inc dword[eax]
        pop ebp
    ret