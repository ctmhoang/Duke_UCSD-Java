let tmp = new SimpleImage('chapel.png');
function makeGray(simpleImage){
    let res = new SimpleImage(simpleImage.getWidth(), simpleImage.getHeight());
    let x = 0, y = 0;
    for(let pixel of simpleImage.values()){
        const avgRGB = (pixel.getRed() + pixel.getBlue() + pixel.getGreen()) / 3;
        if(x >= res.getWidth()){
            x = 0;
            ++y;
        }
        let px = res.getPixel(x++, y);
        px.setGreen(avgRGB);
        px.setRed(avgRGB);
        px.setBlue(avgRGB);
    }
    return res;
}

print(makeGray(tmp));
print(tmp);
