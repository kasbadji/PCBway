Set oWS = WScript.CreateObject("WScript.Shell")
sLinkFile = oWS.SpecialFolders("Desktop") & "\PCBway.lnk"
Set oLink = oWS.CreateShortcut(sLinkFile)
oLink.TargetPath = "C:\Users\kefif\OneDrive\Desktop\studies\ihm\test\PCBway\PCBway.bat"
oLink.WorkingDirectory = "C:\Users\kefif\OneDrive\Desktop\studies\ihm\test\PCBway"
oLink.Description = "PCBway Application"
oLink.Save
WScript.Echo "Desktop shortcut created successfully!"
