const bgImg = new SimpleImage("dinos.png");
const fgImg = new SimpleImage("drewRobert.png");

let out = new SimpleImage(bgImg.getWidth(), bgImg.getHeight());

for(let px of fgImg.values()){
    const x = px.getX();
    const y = px.getY();
    
    if(px.getGreen() > px.getRed() + px.getBlue()){
        let outPx = bgImg.getPixel(x,y);
        out.setPixel(x,y,outPx);
    }
    else{
        out.setPixel(x,y,px);
    }
}

print(out);
