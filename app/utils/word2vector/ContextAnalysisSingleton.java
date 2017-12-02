package utils.word2vector;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.EndingPreProcessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

public class ContextAnalysisSingleton{
	private volatile Word2Vec vec;
	private Config config;

	private static ContextAnalysisSingleton instance = null;
	//new ContextAnalysisSingleton();
	private ContextAnalysisSingleton(){
	}
	public static ContextAnalysisSingleton getInstance(){
		if(instance == null){
			instance = new ContextAnalysisSingleton();
			instance.initContextAnalysisSingleton();
		}
		return instance;
	}

	public void initContextAnalysisSingleton() {

		config = ConfigFactory.load();
		String filePath = config.getString("word2vector.model.path");

        SentenceIterator iter = new LineSentenceIterator(new File(config.getString("word2vector.wakachi.input")));
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
		});
        final EndingPreProcessor preProcessor = new EndingPreProcessor();
        TokenizerFactory tokenizer = new DefaultTokenizerFactory();

		int batchSize = 1000;
		int iterations = 3;
		int layerSize = 150;

		File f = new File(filePath);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
//		cal.get
		if(f.exists() && f.lastModified() > cal.getTimeInMillis())
		{
			// 昔のデータを採用する。
			vec = WordVectorSerializer.readWord2VecModel(f);
		}else {

			vec = new Word2Vec.Builder()
					.batchSize(batchSize) //ミニバッチごとの語数
					.minWordFrequency(5) //
					.useAdaGrad(false) //
					.layerSize(layerSize) // 語の特徴量ベクトルのサイズ
					.iterations(iterations) // トレーニングするイテレーション数
					.learningRate(0.025) //
					.minLearningRate(1e-3) // 学習率は、語数が減るごとに低下します。これは、学習率の最低限度を表します。
					.negativeSample(10) // 10語のサンプルサイズ
					.iterate(iter) //
					.tokenizerFactory(tokenizer)
					.build();
			vec.fit();
			WordVectorSerializer.writeWord2VecModel(vec, filePath);
		}
	}
	public Collection<String> getNearest(String key, int num)
	{
		if(vec == null){
			initContextAnalysisSingleton();
		}
		return vec.wordsNearest(key,num);
	}

	public Collection<String> getNearest(String positive, String negative, int num){
		if(vec == null){
			initContextAnalysisSingleton();
		}
		return vec.wordsNearest(Arrays.asList(positive), Arrays.asList(negative),num);
	}
}
