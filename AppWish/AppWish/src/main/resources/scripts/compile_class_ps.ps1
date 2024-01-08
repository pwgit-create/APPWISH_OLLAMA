#compile_class_ps.ps1
#Usage:PS compile_class_ps.ps1 pathToJavaFile "c:\example_path"
param($pathToJavaFile)
$stdout = "stdout.txt"
Start-Process javac -Args $pathToJavaFile -RedirectStandardError $stdout -Wait
$output = Get-Content $stdout
Write-Output $output
Remove-Item $stdout


