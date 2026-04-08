@echo off
setlocal
cd /d "c:\Users\anwar\Desktop\sid\bookstore-api"
echo --- Daily GitHub Sync Started: %date% %time% ---
git pull origin main --rebase
git add .
git commit -m "Bookstore API — Back-end RESTful service for managing books and authors: %date% %time%"
git push origin main
echo --- Sync Completed ---
pause
