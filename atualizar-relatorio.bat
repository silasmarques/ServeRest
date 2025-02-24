git checkout gh-pages
copy reports\report.html index.html /Y
git add index.html
git commit --allow-empty -m "Forçando atualização do relatório"
git push origin gh-pages
git checkout main