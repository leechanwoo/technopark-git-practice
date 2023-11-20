
package inference.server;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import ai.onnxruntime.NodeInfo;
import ai.onnxruntime.TensorInfo;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;
import ai.onnxruntime.OrtSession.SessionOptions;
import ai.onnxruntime.OrtSession.SessionOptions.OptLevel;
import ai.onnxruntime.OrtProvider;

class ImageClassifier {
    private OrtSession pproc_sess;
    private OrtSession model_sess;
    private BufferedImage decoded_image;
    private float[] float_image;
    private OrtEnvironment env; 
    private Map<String, OnnxTensor> input_args; 

    public ImageClassifier() { 
        this.env = OrtEnvironment.getEnvironment();
        this.input_args = new HashMap();
    }

    public Map<String, OnnxTensor> getInputArgs() {
        return input_args;
    }

    public BufferedImage getDecodedImage() {
        return decoded_image;
    }

    public float[] getFloatImage() {
        return float_image;
    }

    public OrtSession getPProcSession() {
        return pproc_sess;
    }
    public OrtSession getModelSession() {
        return model_sess;
    }

    public void initModelSession(String path) {
        try {
            model_sess = initSession(path);
        } catch (Exception e) {
            System.out.println(e);
            model_sess = null;
        }
    }

    public void initPProcSession(String path) {
        try {
            pproc_sess = initSession(path);
        } catch (Exception e) {
            System.out.println(e);
            pproc_sess = null;
        }
    }

    public float[][] forward_pass_neuralnet(float[][][][] pproc_image) throws OrtException {
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, pproc_image);
        String inputName = model_sess.getInputNames().iterator().next();
        Result outputs = model_sess.run(Collections.singletonMap(inputName, inputTensor));
        return (float[][])outputs.get(0).getValue();
    }
    
    public float[][][][] forward_pass_preprocessing() throws OrtException {
        Result pproc_result = pproc_sess.run(input_args);
        return (float[][][][])pproc_result.get(0).getValue();
    }


    public void makeInputArgs() throws OrtException {
        long[] input_shape = getModelInputShape();
        long[] org_shape = { 1, 3, 
            decoded_image.getWidth(), 
            decoded_image.getHeight()
        };

        OnnxTensor data_tensor = OnnxTensor.createTensor(env, float_image);
        OnnxTensor org_shape_tensor = OnnxTensor.createTensor(env, (Object)org_shape);
        OnnxTensor input_shape_tensor = OnnxTensor.createTensor(env, (Object)input_shape);
        
        input_args.put("RawImg", data_tensor);
        input_args.put("shape", org_shape_tensor);
        input_args.put("sizes", input_shape_tensor);
    }

    public void bufferToFloatImage() throws IOException {
        int width = decoded_image.getWidth();
        int height = decoded_image.getHeight();

        float_image = new float[width * height * 3];
        for (int i = 0; i < float_image.length; i++) {
            float_image[i] = -1;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = decoded_image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                float_image[y*width + x] = (float)r;
                float_image[width*height + y*width + x] = (float)g; 
                float_image[2*width*height + y*width + x] = (float)b; 
            }
        }
    }

    public void base64ToImage(String base64) throws IOException {
        Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(base64);
        ByteArrayInputStream decodedByteImage = new ByteArrayInputStream(decodedBytes);
        decoded_image = ImageIO.read(decodedByteImage);
    }

    public long[] getModelInputShape() throws OrtException { 
        String inputName = model_sess.getInputNames().iterator().next();
        TensorInfo inputTensorInfo = (TensorInfo)model_sess
                                        .getInputInfo()
                                        .get(inputName)
                                        .getInfo();
        long[] input_shape = inputTensorInfo.getShape();
        input_shape[0] = 1;
        return input_shape;
    }

    private OrtSession initSession(String path) throws OrtException, IOException {
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new SessionOptions();
        opts.setOptimizationLevel(OptLevel.BASIC_OPT);
        return env.createSession(path, opts);
    }
    
}