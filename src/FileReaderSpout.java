
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class FileReaderSpout implements IRichSpout {

    private SpoutOutputCollector _collector;
    private TopologyContext context;

    private BufferedReader br;

    @Override
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {

        Objects.requireNonNull(conf);
        Objects.requireNonNull(conf.get("inputFile"));

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    Paths.get(conf.get("inputFile").toString()).toFile().getAbsolutePath())));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.context = context;
        this._collector = collector;
    }

    @Override
    public void nextTuple(){

        String line;
        try {
            if ((line = br.readLine()) != null) {
                _collector.emit(Arrays.asList(new Object[]{line}));

            }else try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

        declarer.declare(new Fields("word"));

    }

    @Override
    public void close() {

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void ack(Object msgId) {
    }

    @Override
    public void fail(Object msgId) {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
