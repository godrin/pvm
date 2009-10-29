# example:
# ::ParseError 
class Colon2Code
  def init
    @t=tmpVar
  end

  def before
    #pp self
    #exit
    @above.before+
    s("#{@t}=#{@above.value}.#{@name}")+
    @above.after
  end

  def after
    s("clear #{@t}")
  end

  def value
    #raise 1
    @t
  end
  alias :name :value
end