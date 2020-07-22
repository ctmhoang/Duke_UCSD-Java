function setBlack(pixel){
    pixel.setBlue(0);
    pixel.setRed(0);
    pixel.setGreen(0);
    return pixel;
}

function addBorder(img , thickness){
    const w = img.getWidth();
    const h = img.getHeight();
    
    for(let i = 0 ; i < w; i ++){
        for(let j = 0; j < thickness; j ++ ){
            img.setPixel(i, 0 + j, setBlack(img.getPixel(i, 0+j)));
            img.setPixel(i, h - j -1, setBlack(img.getPixel(i, h - j -1)));
        }
        
        for(let i = thickness, k = h - thickness ; i < k ; i++){
            for(let j = 0 ; j < thickness; j ++){
                img.setPixel(0+j, i, setBlack(img.getPixel(0+j, i)));
                img.setPixel(w-j-1, i, setBlack(img.getPixel(w-j-1, i)));
            }
        }
    }
}

let img = new SimpleImage("smallpanda.png");
addBorder(img,10);
print(img);

