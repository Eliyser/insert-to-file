package com.haien.insert;

import java.io.*;

/**
 * @Author haien
 * @Description 批量向md文档插入标题
 * @Date 2019/5/3
 **/
public class InsertNewContent {
    public static void main(String[] args) throws IOException {
        //创建临时文件
        File tempFile=File.createTempFile("temp",null);
        //虚拟机终止时，删除此文件
        tempFile.deleteOnExit();

        //遍历父目录下all md文档
        StringBuilder parentPath=new StringBuilder("E:/hexo2/source/_posts/");
        File parent=new File(parentPath.toString());
        String prefix="---\ntitle: ";
        String subfix="\n---\n\n";
        String filename;
        String filePath;
        if(parent.isDirectory()){
            for (String subName:parent.list()) {
                //拼接出每个文件的新内容
                filename=subName.substring(0,subName.lastIndexOf("."));
                String content=prefix+filename+subfix;
                filePath=parentPath+subName;
                insert(filePath,0L,content,tempFile);
            }
        }

        System.out.println("处理成功！");
    }

    /**
     * @Author haien
     * @Description 插入内容：需要先把目标位置后面的内容读入缓存文件，追加新内容后再写回来
     * @Date 2019/5/3
     * @Param [filename, pos, content]
     * @return void
     **/
    private static void insert(String filePath, long pos,
                               String content, File tempFile)
            throws IOException {
        //把原文件写往临时文件
        RandomAccessFile raf=new RandomAccessFile(filePath,"rw");
        FileOutputStream fos=new FileOutputStream(tempFile);
        raf.seek(pos);
        byte[] buffer=new byte[1024];
        int num=0;
        while (-1 != (num=raf.read(buffer))) {
            fos.write(buffer,0,num);
        }
        fos.flush();

        //插入内容
        raf.seek(pos);
        raf.write(content.getBytes());

        //把原内容写回来
        FileInputStream fis=new FileInputStream(tempFile);
        while (-1 != (num=fis.read(buffer))) {
            raf.write(buffer,0,num);
        }

        fis.close();
        fos.close();
    }
}








































