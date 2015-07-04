Set oWS = WScript.CreateObject("WScript.Shell") 
sLinkFile = "C:\Program Files\FZUtils\imageupdater.lnk" 
Set oLink = oWS.CreateShortcut(sLinkFile) 
   oLink.TargetPath = "C:\Program Files\FZUtils\imageupdater.exe" 
   oLink.IconLocation = "C:\Program Files\FZUtils\imageupdater.exe, 1" 
 '  oLink.Arguments = "" 
 '  oLink.Description = "Image Updater"   
 '  oLink.HotKey = "ALT+CTRL+U" 
 '  oLink.WindowStyle = "1" 
 '  oLink.WorkingDirectory = "" 
oLink.Save 
