package pn.cg.app_system.code_generation.model;

/**
 * Record for the Ollama config options that you can edit in app wish
 * @param num_ctx
 * @param top_k
 * @param num_predict
 * @param temperature
 * @param useExternalLibraries
 */
public record OllamaConfig(int num_ctx,int top_k,int num_predict,float temperature,boolean useExternalLibraries){}
