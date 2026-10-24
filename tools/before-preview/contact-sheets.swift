import AppKit
import Foundation

let folder = URL(fileURLWithPath: CommandLine.arguments[1])
let data = try Data(contentsOf: folder.appendingPathComponent("screens.json"))
let rows = try JSONSerialization.jsonObject(with: data) as! [[String:String]]
let columns = 4, perPage = 8
let tileW = 270, imageW = 230, imageH = 498, tileH = 590
let width = columns * tileW + 48, height = 2 * tileH + 150
func label(_ value: String, _ rect: NSRect, size: CGFloat, color: NSColor = .black) {
    (value as NSString).draw(in: rect, withAttributes: [.font:NSFont.systemFont(ofSize:size,weight:.medium),.foregroundColor:color])
}
for page in 0..<Int(ceil(Double(rows.count)/Double(perPage))) {
    let canvas = NSImage(size: NSSize(width: width, height: height))
    canvas.lockFocus()
    NSColor(calibratedWhite:0.95,alpha:1).setFill(); NSRect(x:0,y:0,width:width,height:height).fill()
    label("CEKPEDIA / BEFORE",NSRect(x:32,y:height-52,width:900,height:30),size:26)
    label("Layout asli · data contoh offline  |  22 Oktober 2026  |  \(page+1)/3",NSRect(x:32,y:height-84,width:1000,height:22),size:14,color:.darkGray)
    for local in 0..<perPage {
        let index=page*perPage+local
        if index >= rows.count {break}
        let row=rows[index], x=24+(local%columns)*tileW, y=height-110-(local/columns+1)*tileH
        NSColor.white.setFill(); NSRect(x:x,y:y,width:tileW-12,height:tileH-12).fill()
        if let img=NSImage(contentsOf:folder.appendingPathComponent(row["file"]!)) {
            img.draw(in:NSRect(x:x+14,y:y+72,width:imageW,height:imageH))
        }
        label("\(index+1). \(row["title"]!)",NSRect(x:x+14,y:y+38,width:tileW-35,height:30),size:13)
        label(index==0 ? "APK ASLI / RUNTIME" : "PREVIEW LAYOUT / OFFLINE",NSRect(x:x+14,y:y+13,width:tileW-30,height:22),size:10,color:.darkGray)
    }
    label("Banner Firebase kosong; peta placeholder. Buka galeri untuk sumber dan batas tiap screenshot.",NSRect(x:32,y:20,width:1000,height:20),size:12,color:.darkGray)
    canvas.unlockFocus()
    let bitmap=NSBitmapImageRep(data:canvas.tiffRepresentation!)!
    let png=bitmap.representation(using:.png,properties:[:])!
    try png.write(to:folder.appendingPathComponent("overview-\(page+1).png"))
}
