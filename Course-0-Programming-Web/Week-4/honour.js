function crop(image, width, height){
    if(image.getWidth() < width || image.getHeight() < height){
        window.alert('image has size smaller than cropped image');
        return null;
    }
    let img = new SimpleImage(width,height);
    for(let x = 0 ; x < width; x++){
        for(let y = 0 ; y < height; y ++){
            img.setPixel(x,y,image.getPixel(x,y));
        }
    }
    return img;
}

function clearBits(colorval){
    return Math.floor(colorval / 16) * 16;
}

function chop2Hide(img){
    for(let px of img.values()){
        px.setBlue(clearBits(px.getBlue()));
        px.setRed(clearBits(px.getRed()));
        px.setGreen(clearBits(px.getGreen()));
    }
}

function shift(img){
    for(let px of img.values()){
        px.setBlue(px.getBlue() / 16);
        px.setRed(px.getRed() / 16);
        px.setGreen(px.getGreen()/16);
    }
}

function combine(show,hide){
    const ws = show.getWidth();
    const wh = hide.getWidth();
    const hs = show.getHeight();
    const hh = hide.getHeight();
    let showImg = show;
    let hideImg = hide;
    let res;
    if(ws == wh && hs == hh)
    {
        res = new SimpleImage(wh,hh);
    }
    else if(ws < wh && hs < hh){
        hideImg = crop(hide,ws,hs);
        res = new SimpleImage(ws,hs);
    }else if(ws > wh && hs > hh){
        showImg = crop(show,wh,hh);
        res = new SimpleImage(wh,hh);
    }else{
        const width = ws < wh ? ws : wh;
        const height = hs < hh ? hs : hh;
        showImg = crop(show,width,height);
        hideImg = crop(hide,width,height);
        res = new SimpleImage(width,height);
    }
    
    shift(hideImg);
    chop2Hide(showImg);
    
    for(let px of res.values()){
        const x = px.getX();
        const y = px.getY();
        
        const showPx = showImg.getPixel(x,y);
        const hidePx = hideImg.getPixel(x,y);
        
        px.setRed(showPx.getRed() + hidePx.getRed());
        px.setGreen(showPx.getGreen() + hidePx.getGreen());
        px.setBlue(showPx.getBlue() + hidePx.getBlue());

    }
    return res;
}

let hide = new SimpleImage('duvall.jpg');
let show = new SimpleImage('chapel.png');
let res = combine(show,hide);

print(res);

function extract(encrypted){
    let res = new SimpleImage(encrypted.getWidth(), encrypted.getHeight());
    for(let px of res.values()){
        const x = px.getX();
        const y = px.getY();
        
        const showPx = encrypted.getPixel(x,y);
        
        px.setRed(showPx.getRed() % 16 * 16);
        px.setGreen(showPx.getGreen() % 16 * 16);
        px.setBlue(showPx.getBlue() % 16 * 16);
    }
    return res;
}

let hiddenImage = extract(res);

print(hiddenImage);
