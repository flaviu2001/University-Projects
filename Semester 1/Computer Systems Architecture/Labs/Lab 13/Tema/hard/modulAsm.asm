bits 32
global _reverse
segment data public data use32
    branza resd 1
    gata db 0
segment code public code use32
    _reverse:
        push ebp
        mov ebp, esp
        ; [esp] - ebp
        ; [esp+4] - adresa de return
        ; [esp+8] - sir
        mov esi, [esp+8]
        cmp byte[esi], 0
        cld
        je finish
            aici:
            mov edi, esi
                cuvant:
                lodsb
                cmp al, ' '
                je loopend
                cmp al, 0
                mov byte[gata], 1
                je loopend
                mov byte[gata], 0
                jmp cuvant
            loopend:
            mov [branza], esi
            sub esi, 2
            xchg esi, edi
                imnotgoodwithlabels:
                cmp esi, edi
                jae loopend2
                    mov al, [edi]
                    mov bl, [esi]
                    mov [esi], al
                    mov [edi], bl
                    inc esi
                    dec edi
                    jmp imnotgoodwithlabels
                loopend2:
            mov esi, [branza]
            cmp byte[gata], 0
            je aici
        finish:
        mov esp, ebp
        pop ebp
        ret