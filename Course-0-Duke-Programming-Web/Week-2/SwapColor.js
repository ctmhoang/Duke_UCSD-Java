function swapRedGreen(pixel){
    const r = pixel.getRed();
    pixel.setRed(pixel.getGreen());
    pixel.setGreen(r);
}

let img = new SimpleImage('smallhands.png');
for(let px of img.values()){
    swapRedGreen(px);
}
print(img);