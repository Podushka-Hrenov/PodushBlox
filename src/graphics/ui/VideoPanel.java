package telegram.bot.graphics.ui;

import java.io.File;
import java.util.function.Function;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegFrameRecorder.Exception;
import org.bytedeco.javacv.Frame;

import telegram.bot.graphics.math.Vec2Int;

public class VideoPanel {
    public final FFmpegFrameRecorder recorder;
    private int fps;
    
    public VideoPanel(Vec2Int size, int fps, int bitrate, File outputFile) {
        recorder = new FFmpegFrameRecorder(outputFile, size.x, size.y);
        recorder.setFrameNumber(fps);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setVideoBitrate(bitrate);

        this.fps = fps;
    }

    public void record(Function<Integer, Frame> frameFabric,
    float duration) throws Exception {

        recorder.start();

        for (int frameNum = 1; frameNum <= fps * duration; frameNum++) {
            recorder.record(frameFabric.apply(frameNum));
        }

        recorder.stop();
        recorder.release();
    }
}
