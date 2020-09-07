function convertToYellow(pixel){
    let r = pixel.getRed();
    let b = pixel.getBlue();
    if(r == 0) r = 1;
    if(b == 0) b = 1;
    if(b/r > 1){
        pixel.setRed(255);
        pixel.setBlue(0);
        pixel.setGreen(255);
    }
}

let img = new SimpleImage('duke_blue_devil.png');
for(let px of img.values()){
    convertToYellow(px);
}
print(img);