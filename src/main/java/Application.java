import com.kaidi.swallow.Liar;
import com.kaidi.swallow.LogicException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

/**
 * Created by kaidi on 17-1-17.
 */
public class Application {

    public static void main(String[] args) {
        try {
            Liar liar = new Liar();
            String path = "/home/kaidi/study/";
            List<File> files = new ArrayList<>();

            liar.scan(path, files);
            System.out.println(files.size());
            for (File f : files) {
                System.out.println(f.getPath());
            }

        } catch (IOException e) {

        } //catch (LogicException e) {
        //System.out.println("参数错误.程序未运行");
        catch (Exception e) {
            System.out.println("程序出现异常,请检查文件是否受损.");
        }
    }
}
