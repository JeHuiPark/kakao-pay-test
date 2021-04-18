package kakao.pay.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;

@Import(MockMvcTestBased.PrintEncodingConfiguration.class)
@AutoConfigureMockMvc
public class MockMvcTestBased {

  @Autowired
  protected MockMvc mockMvc;

  public static class PrintEncodingConfiguration implements MockMvcBuilderCustomizer {

    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
      builder.addFilters(new CharacterEncodingFilter("UTF-8", true));
    }
  }
}
