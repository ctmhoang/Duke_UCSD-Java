let img = new SimpleImage("hilton.jpg");
const w = img.getWidth();
const perPart = w/3;
for(let px of img.values()){
    const x = parseInt(px.getX() / perPart);
    switch(x){
        case 0:
            px.setRed(255);
            break;
        case 1:
            px.setGreen(255);
            break;
        default:
            px.setBlue(255);
    }
}
print(img);