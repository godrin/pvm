# example:
# ::ParseError 
class Colon3Code
  def init
    @t=tmpVar
  end

  def before
    #pp self
    #exit
    s("#{@t}=::#{@name}")
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