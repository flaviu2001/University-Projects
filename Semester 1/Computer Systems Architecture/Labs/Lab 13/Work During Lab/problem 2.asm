bits 32

global start        

extern exit               
import exit msvcrt.dll    
                          

extern printf
import printf msvcrt.dll
extern fopen
import fopen msvcrt.dll
extern fread
import fread msvcrt.dll
extern fclose
import fclose msvcrt.dll
                          
segment data use32 class=data
    file_name db "file.txt", 0
    access_mode db "r", 0
    sir times 100 db 0
    buffer db 0
    no_words dd 0
    first_word times 100 db 0
    len_first_word dd 0
    fd resd 1
    format db "Nr cuvinte %d, primul cuvant %s are %d litere", 10, 0

segment code use32 class=code
    start:
        
        push access_mode
        push file_name
        call [fopen]
        add esp, 4*2
        
        mov [fd], eax
        cmp eax, 0
        je _404
        
        mov edi, sir
        
        loop_label:
            
            push dword[fd]
            times 2 push 1
            push buffer
            call [fread]
            add esp, 4*4
            
            
            cmp eax, 0
            je _404                
            
            mov al, [buffer]
            stosb
            
            cmp al, '.'
            jne after_comma
                sub edi, 2
                mov esi, sir
                
                mov dword[no_words], 1
                words:
                    cmp esi, edi
                    ja after_words
                    cmp byte[esi], ' '
                    jne prost
                        inc dword[no_words]
                    prost:
                    inc esi
                    jmp words
                after_words:
                
                mov esi, sir
                mov edi, first_word
                label1:
                    lodsb
                    cmp al, ' '
                    je ceva
                    stosb
                    jmp label1
                ceva:
                mov byte[edi], 0
                sub edi, first_word
                mov dword[len_first_word], edi
                add edi, first_word
                
                mov esi, first_word
                dec edi
                
                swap:
                    cmp esi, edi
                    jae over
                    mov dl, [esi]
                    mov cl, [edi]
                    mov [esi], cl
                    mov [edi], dl
                    inc esi
                    dec edi
                    jmp swap
                over:
                
                push dword[len_first_word]
                push first_word
                push dword[no_words]
                push format
                call [printf]
                add esp, 4*4
                
                mov edi, sir
            after_comma:
        jmp loop_label
        
        _404:
        push dword[fd]
        call [fclose]
        add esp, 4*1
        push    dword 0
        call    [exit]
