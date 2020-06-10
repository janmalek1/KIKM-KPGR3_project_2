#version 460                        /*aktuální verze*/

in vec2 texCoord;

uniform int mode;                   /* 0 - ORIGINÁLNÍ FOTKA, 1 - NAIVE DITHERING, 2 - RANDOM DITHERING, 3 ORDERED DITHERING */
uniform int size;                   /* velikost matice pro ORDERED DITHERING: 4 (tj. 4x4), 8 (tj. 8x8)*/
uniform float scale;                /* granularita obrázku pro ORDERED DITHERING */
uniform float thresholdNaive;       /* nastavuje threshold pro NAIVE DITHERING*/
uniform float thresholdRandom;      /* nastavuje threshold pro RANDOM DITHERING*/

uniform sampler2D textureMosaic;

out vec4 outColor;

/**
 * matice 4x4 pro ordered dithering
 * @source https://en.wikipedia.org/wiki/Ordered_dithering

 */
int ditherMat4[4][4] = {
{ 0, 12, 3, 15},
{ 8, 4, 11, 7},
{ 2, 14, 1, 13},
{ 10, 6, 9, 5}
};

/**
 * matice 8x8 pro ordered dithering
 * @source https://en.wikipedia.org/wiki/Ordered_dithering

 */
int ditherMat8[8][8] = {
{ 0, 32, 8, 40, 2, 34, 10, 42},
{48, 16, 56, 24, 50, 18, 58, 26},
{12, 44, 4, 36, 14, 46, 6, 38},
{60, 28, 52, 20, 62, 30, 54, 22},
{ 3, 35, 11, 43, 1, 33, 9, 41},
{51, 19, 59, 27, 49, 17, 57, 25},
{15, 47, 7, 39, 13, 45, 5, 37},
{63, 31, 55, 23, 61, 29, 53, 21} };

/**
 * generátor náhodných čísel pro účely náhodného ditheringu
 * @source https://thebookofshaders.com/

 */
/*tohle zde používám, protože jsem nenašel zabudovanou GLSL funkci na generování náhodných čísel*/
/*v popisu funkce psali, že má generovat hodnoty v intervalu [0,1], ale experimentálně jsem ověřil, že to není pravda*/
float rnd(vec2 x)
{
    int n = int(x.x * 40.0 + x.y * 6400.0);
    n = (n << 13) ^ n;
    return 1.0 - float( (n * (n * n * 15731 + 789221) + \
             1376312589) & 0x7fffffff) / 1073741824.0;
}

float limitValue(int mode, int size, vec2 xy)
{
    float valReturn;

    if (mode == 1) {
        valReturn = thresholdNaive;
    } else if (mode == 2) {
        valReturn = rnd(xy) + thresholdRandom;
    } else if (mode == 3) {

        int x = int(mod(xy.x * scale, size)) ;
        int y = int(mod(xy.y * scale, size)) ;

        if (size == 4) {
            valReturn = (ditherMat4[x][y]+1)/16.0f;
        } else if (size == 8) {
            valReturn = (ditherMat8[x][y]+1)/64.0f;
        }
    }

    return valReturn;
}

void main() {

    vec3 textureColor = texture2D(textureMosaic, texCoord).rgb;
    vec2 xy = gl_FragCoord.xy;

    vec3 finalColor;

    float limit;

    if (mode == 0) {
    // 0 - ORIGINÁLNÍ FOTKA, levý horní roh
        finalColor = textureColor;
    } else {
    // 1 - NAIVE DITHERING, pravý horní roh
    // 2 - RANDOM DITHERING, levý dolní roh
    // 3 - ORDERED DITHERING, prový dolní roh
        finalColor.r=0;
        finalColor.g=0;
        finalColor.b=0;

        limit = limitValue(mode,size,xy);

        if(textureColor.r > limit) finalColor.r=1;
        if(textureColor.g > limit) finalColor.g=1;
        if(textureColor.b > limit) finalColor.b=1;
    }

    outColor = vec4(finalColor,1) ;

} 
