class ReturnCode
  def init
    @v=tmpVar
  end

  def code
    @what.before+
    s("#{@v}=#{@what.value}")+
    @what.after+
    s("freturn #{@v}")
  end
  
  def value
    @v
  end

  def after
    s("clear #{@v}")
  end
end