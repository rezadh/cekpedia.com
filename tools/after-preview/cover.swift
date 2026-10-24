import AppKit
import Foundation

let folder=URL(fileURLWithPath:CommandLine.arguments[1])
let canvas=NSImage(size:NSSize(width:1120,height:940))
canvas.lockFocus()
NSColor(red:0.965,green:0.973,blue:0.961,alpha:1).setFill()
NSRect(x:0,y:0,width:1120,height:940).fill()
func text(_ value:String,_ rect:NSRect,_ size:CGFloat,_ weight:NSFont.Weight = .regular) {
 (value as NSString).draw(in:rect,withAttributes:[.font:NSFont.systemFont(ofSize:size,weight:weight),.foregroundColor:NSColor(red:0.075,green:0.247,blue:0.212,alpha:1)])
}
text("cekpedia / after",NSRect(x:40,y:867,width:1000,height:42),34,.bold)
text("Tampilan baru untuk cerita berikutnya.  •  Preview offline, data contoh",NSRect(x:40,y:833,width:1040,height:26),15)
let screens=[("02-beranda.png","01 / BERANDA"),("15-apotek.png","02 / DAFTAR TEMPAT"),("21-profil.png","03 / RUANG JELAJAHMU")]
for (index,item) in screens.enumerated() {
 let x=40+index*360
 if let image=NSImage(contentsOf:folder.appendingPathComponent(item.0)) {
  image.draw(in:NSRect(x:x,y:100,width:320,height:693))
 }
 text(item.1,NSRect(x:x,y:54,width:330,height:24),13,.semibold)
}
canvas.unlockFocus()
let bitmap=NSBitmapImageRep(data:canvas.tiffRepresentation!)!
try bitmap.representation(using:.png,properties:[:])!.write(to:folder.appendingPathComponent("preview.png"))
