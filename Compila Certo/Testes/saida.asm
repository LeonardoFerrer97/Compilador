sseg SEGMENT STACK ;início seg. pilha 
byte 4000h DUP(?)  ;dimensiona pilha
sseg ENDS ;fim seg. pilha
dseg SEGMENT PUBLIC ;início seg. dados
byte 4000h DUP(?) ;temporários
mov al, 20
mov DS: [0h] , al
byte 20 DUP(?) ; vetor luigi em 16384
byte 0x5f ; caractere 0x5f
sword 5 ; inteiro 5
dseg ends  ; fim seg.dados
mov ax, DS: [16405h] 
mov bx, DS: [16405h] 
add ax,bx
mov DS: [16405h],ax
