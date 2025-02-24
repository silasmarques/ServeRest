#!/bin/bash
git checkout gh-pages
cp reports/report.html ./index.html
git add index.html
git commit --allow-empty -m "Forçando atualização do relatório"
git push origin gh-pages
git checkout main