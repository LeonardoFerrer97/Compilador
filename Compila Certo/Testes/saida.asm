sseg SEGMENT STACK ;in�cio seg. pilha 
byte 4000h DUP(?)  ;dimensiona pilha
sseg ENDS ;fim seg. pilha
dseg SEGMENT PUBLIC ;in�cio seg. dados
byte 4000h DUP(?) ;tempor�rios
mov al, 20
mov DS: [0] , al
byte 20 DUP(?) ; vetor luigi em 16384
byte 0x5f ; caractere 0x5f
dseg ends  ; fim seg.dados
