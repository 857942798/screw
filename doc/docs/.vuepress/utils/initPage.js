const fs = require('fs');
// 排除检查的文件
let excludes = ['.DS_Store']
let FileHelper = {
    getFileName: function (rpath) {
        let fileNames = [];
        let fileTypes = /\.md$/; //只匹配以md结尾的文件
        fs.readdirSync(rpath).forEach(file => {
            if (excludes.indexOf(file) < 0) {
                let fullPath = rpath + "/" + file
                let fileInfo = fs.statSync(fullPath)
                if (fileInfo.isFile()) {
                    if (fileTypes.test(file) > 0) {
                        if (file === '1_0.md') {
                            file = '';
                        } else {
                            file = file.replace('.md', '');
                        }
                        fileNames.push(file);
                    }
                }
            }
        })
        // console.log(filenames)
        fileNames.sort(); // 排序
        return fileNames;
    }
}
module.exports = FileHelper;
