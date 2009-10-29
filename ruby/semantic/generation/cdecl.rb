# const declare
class CdeclCode
  def init
    @v=tmpVar
  end

  def before
    @rval.before+
    s("#{@v}=#{@rval.value}")+
    s("#{@name}=#{@v}")+
    s("clear #{@v}")+
    @rval.after
  end

  def after
    []
  end
  
  def value
    @v
  end
  
  def code
    []
  end
end