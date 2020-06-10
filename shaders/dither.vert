#version 460                        /*aktuální verze*/
in vec2 inPosition;

uniform int mode;                   /* 0 - ORIGINÁLNÍ FOTKA, 1 - NAIVE DITHERING, 2 - RANDOM DITHERING, 3 - ORDERED DITHERING */
uniform int size;                   /* velikost matice pro ORDERED DITHERING: 4 (tj. 4x4), 8 (tj. 8x8)*/
uniform float scale;                /* granularita obrázku pro ORDERED DITHERING */

out vec2 texCoord;

//const int mode = 2;

void main() {

    vec2 position;

    if (mode == 0) { // 0 - ORIGINÁLNÍ FOTKA, levý horní roh
        position = vec2(inPosition.x - 1, inPosition.y);
    }
    if (mode == 1) { // 1 - NAIVE DITHERING, pravý horní roh
        position = vec2(inPosition.x, inPosition.y);
    }
    if (mode == 2) { // 2 - RANDOM DITHERING, levý dolní roh
        position = vec2(inPosition.x - 1, inPosition.y - 1);
    }
    if (mode == 3) { // 3 - ORDERED DITHERING, prový dolní roh
        position = vec2(inPosition.x, inPosition.y - 1);
    }

    gl_Position = vec4(position, 1.0, 1.0);

    //texCoord = inPosition;
    /*obrázek byl původně vidět vzhůru nohama, proto zde provádím otočení*/
    texCoord = 1 - inPosition;

} 
